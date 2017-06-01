/*
 * Copyright 2012-2017 CodeLibs Project and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.codelibs.fess.thumbnail.impl;

import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import org.codelibs.core.lang.StringUtil;
import org.codelibs.core.misc.Tuple3;
import org.codelibs.elasticsearch.runner.net.Curl;
import org.codelibs.fess.mylasta.direction.FessConfig;
import org.codelibs.fess.util.ComponentUtil;
import org.codelibs.fess.util.DocumentUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HtmlTagBasedGenerator extends BaseThumbnailGenerator {

    private static final Logger logger = LoggerFactory.getLogger(HtmlTagBasedGenerator.class);

    @Override
    public void destroy() {
    }

    @Override
    public Tuple3<String, String, String> createTask(final String path, final Map<String, Object> docMap) {
        final FessConfig fessConfig = ComponentUtil.getFessConfig();
        final String url = DocumentUtil.getValue(docMap, fessConfig.getIndexFieldThumbnail(), String.class);
        if (StringUtil.isBlank(url)) {
            return null;
        }
        return new Tuple3<>(getName(), url, path);
    }

    @Override
    public boolean generate(final String url, final File outputFile) {
        if (logger.isDebugEnabled()) {
            logger.debug("Generate Thumbnail: " + url);
        }

        if (outputFile.exists()) {
            if (logger.isDebugEnabled()) {
                logger.debug("The thumbnail file exists: " + outputFile.getAbsolutePath());
            }
            return true;
        }

        final File parentFile = outputFile.getParentFile();
        if (!parentFile.exists()) {
            parentFile.mkdirs();
        }
        if (!parentFile.isDirectory()) {
            logger.warn("Not found: " + parentFile.getAbsolutePath());
            return false;
        }

        Curl.get(url).execute(con -> {
            try (ImageInputStream input = ImageIO.createImageInputStream(con.getInputStream())) {
                saveImage(input, outputFile);
            } catch (final Throwable t) {
                logger.warn("Failed to convert " + url, t);
            }
        });

        return false;
    }

    protected void saveImage(final ImageInputStream input, final File outputFile) throws IOException {
        final FessConfig fessConfig = ComponentUtil.getFessConfig();
        final Iterator<ImageReader> readers = ImageIO.getImageReaders(input);
        if (readers.hasNext()) {
            final ImageReader reader = readers.next();
            try {
                reader.setInput(input);
                final ImageReadParam param = reader.getDefaultReadParam();
                final int width = reader.getWidth(0);
                final int height = reader.getHeight(0);
                final int samplingWidth = width / fessConfig.getThumbnailHtmlImageThumbnailWidthAsInteger();
                final int samplingHeight = height / fessConfig.getThumbnailHtmlImageThumbnailHeightAsInteger();
                param.setSourceSubsampling(samplingWidth <= 0 ? 1 : samplingWidth, samplingHeight <= 0 ? 1 : samplingHeight, 0, 0);
                param.setSourceRegion(new Rectangle(width, height > width ? width : height));
                final BufferedImage image = reader.read(0, param);
                final int thumbnailWidth = fessConfig.getThumbnailHtmlImageThumbnailWidthAsInteger();
                final int thumbnailHeight =
                        (int) (((float) (height > width ? width : height))
                                * fessConfig.getThumbnailHtmlImageThumbnailWidthAsInteger().floatValue() / (float) width);
                BufferedImage thumbnail = new BufferedImage(thumbnailWidth, thumbnailHeight, image.getType());
                thumbnail.getGraphics().drawImage(image.getScaledInstance(thumbnailWidth, thumbnailHeight, Image.SCALE_AREA_AVERAGING), 0,
                        0, thumbnailWidth, thumbnailHeight, null);
                ImageIO.write(thumbnail, fessConfig.getThumbnailHtmlImageFormat(), outputFile);
                image.flush();
            } finally {
                reader.dispose();
            }
        }
    }

}
