/*
 * Copyright 2012-2016 CodeLibs Project and the Others.
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
package org.codelibs.fess.app.web.admin.webconfig;

import java.io.Serializable;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

import org.codelibs.core.lang.StringUtil;
import org.codelibs.fess.Constants;
import org.codelibs.fess.app.web.CrudMode;
import org.codelibs.fess.util.ComponentUtil;
import org.codelibs.fess.util.StreamUtil;
import org.codelibs.fess.validation.UriType;
import org.lastaflute.web.validation.Required;
import org.lastaflute.web.validation.theme.conversion.ValidateTypeFailure;

/**
 * @author shinsuke
 * @author Shunji Makino
 * @author Keiichi Watanabe
 */
public class CreateForm implements Serializable {

    private static final long serialVersionUID = 1L;

    public String[] roleTypeIds;

    public String[] labelTypeIds;

    @ValidateTypeFailure
    public Integer crudMode;

    @Required
    @Size(max = 200)
    public String name;

    @Required
    @UriType(protocols = { "http:", "https:" })
    @Size(max = 4000)
    public String urls;

    @Size(max = 4000)
    public String includedUrls;

    @Size(max = 4000)
    public String excludedUrls;

    @Size(max = 4000)
    public String includedDocUrls;

    @Size(max = 4000)
    public String excludedDocUrls;

    @Size(max = 4000)
    public String configParameter;

    @Min(value = 0)
    @Max(value = 2147483647)
    @ValidateTypeFailure
    public Integer depth;

    @Min(value = 0)
    @Max(value = 9223372036854775807l)
    @ValidateTypeFailure
    public Long maxAccessCount;

    @Required
    @Size(max = 200)
    public String userAgent;

    @Required
    @Min(value = 0)
    @Max(value = 2147483647)
    @ValidateTypeFailure
    public Integer numOfThread;

    @Required
    @Min(value = 0)
    @Max(value = 2147483647)
    @ValidateTypeFailure
    public Integer intervalTime;

    @Required
    @ValidateTypeFailure
    public Float boost;

    @Required
    @Size(max = 5)
    public String available;

    @Required
    @Min(value = 0)
    @Max(value = 2147483647)
    @ValidateTypeFailure
    public Integer sortOrder;

    @Required
    @Size(max = 1000)
    public String createdBy;

    @Required
    @ValidateTypeFailure
    public Long createdTime;

    public void initialize() {
        crudMode = CrudMode.CREATE;
        boost = 1.0f;
        if (StringUtil.isBlank(userAgent)) {
            userAgent = ComponentUtil.getUserAgentName();
        }
        numOfThread = Constants.DEFAULT_NUM_OF_THREAD_FOR_WEB;
        intervalTime = Constants.DEFAULT_INTERVAL_TIME_FOR_WEB;
        sortOrder = 0;
        createdBy = ComponentUtil.getSystemHelper().getUsername();
        createdTime = ComponentUtil.getSystemHelper().getCurrentTimeAsLong();
        final String roles = ComponentUtil.getFessConfig().getSearchDefaultRoles();
        if (StringUtil.isNotBlank(roles)) {
            roleTypeIds = StreamUtil.of(roles.split(",")).map(role -> role.trim()).toArray(n -> new String[n]);
        }
    }
}
