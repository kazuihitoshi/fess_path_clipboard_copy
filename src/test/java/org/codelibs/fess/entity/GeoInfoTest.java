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
package org.codelibs.fess.entity;

import org.codelibs.fess.unit.UnitFessTestCase;

public class GeoInfoTest extends UnitFessTestCase {

    public void test_0_0_10() {
        final String latitude = "0";
        final String lonitude = "0";
        final String distance = "10";

        final GeoInfo geoInfo = create(latitude, lonitude, distance);
        assertTrue(geoInfo.isAvailable());
        String result = "{\"geo_distance\":{\"geo_info\":[0.0,0.0],\"distance\":\"10.0km\"}}";
        assertEquals(result, geoInfo.toQueryBuilder().toString().replaceAll("[ \n]", ""));
    }

    public void test_90_180_10() {
        final String latitude = "90";
        final String lonitude = "180";
        final String distance = "10";

        final GeoInfo geoInfo = create(latitude, lonitude, distance);
        assertTrue(geoInfo.isAvailable());
        String result = "{\"geo_distance\":{\"geo_info\":[180.0,90.0],\"distance\":\"10.0km\"}}";
        assertEquals(result, geoInfo.toQueryBuilder().toString().replaceAll("[ \n]", ""));
    }

    public void test_91_181_10() {
        final String latitude = "91";
        final String lonitude = "181";
        final String distance = "10";

        final GeoInfo geoInfo = create(latitude, lonitude, distance);
        assertTrue(geoInfo.isAvailable());
        String result = "{\"geo_distance\":{\"geo_info\":[-179.0,90.0],\"distance\":\"10.0km\"}}";
        assertEquals(result, geoInfo.toQueryBuilder().toString().replaceAll("[ \n]", ""));
    }

    public void test_91_361_10() {
        final String latitude = "91";
        final String lonitude = "361";
        final String distance = "100";

        final GeoInfo geoInfo = create(latitude, lonitude, distance);
        assertTrue(geoInfo.isAvailable());
        String result = "{\"geo_distance\":{\"geo_info\":[1.0,90.0],\"distance\":\"100.0km\"}}";
        assertEquals(result, geoInfo.toQueryBuilder().toString().replaceAll("[ \n]", ""));
    }

    public void test__90__180_10() {
        final String latitude = "-90";
        final String lonitude = "-180";
        final String distance = "10";

        final GeoInfo geoInfo = create(latitude, lonitude, distance);
        assertTrue(geoInfo.isAvailable());
        String result = "{\"geo_distance\":{\"geo_info\":[-180.0,-90.0],\"distance\":\"10.0km\"}}";
        assertEquals(result, geoInfo.toQueryBuilder().toString().replaceAll("[ \n]", ""));
    }

    public void test__91__181_10() {
        final String latitude = "-91";
        final String lonitude = "-181";
        final String distance = "10";

        final GeoInfo geoInfo = create(latitude, lonitude, distance);
        assertTrue(geoInfo.isAvailable());
        String result = "{\"geo_distance\":{\"geo_info\":[179.0,-90.0],\"distance\":\"10.0km\"}}";
        assertEquals(result, geoInfo.toQueryBuilder().toString().replaceAll("[ \n]", ""));
    }

    public void test__91__361_10() {
        final String latitude = "-91";
        final String lonitude = "-361";
        final String distance = "100";

        final GeoInfo geoInfo = create(latitude, lonitude, distance);
        assertTrue(geoInfo.isAvailable());
        String result = "{\"geo_distance\":{\"geo_info\":[-1.0,-90.0],\"distance\":\"100.0km\"}}";
        assertEquals(result, geoInfo.toQueryBuilder().toString().replaceAll("[ \n]", ""));
    }

    public void test_0_0_0() {
        final String latitude = "0";
        final String lonitude = "0";
        final String distance = "0";

        final GeoInfo geoInfo = create(latitude, lonitude, distance);
        assertFalse(geoInfo.isAvailable());
    }

    public void test_x_0_0() {
        final String latitude = "x";
        final String lonitude = "0";
        final String distance = "10";

        final GeoInfo geoInfo = create(latitude, lonitude, distance);
        assertFalse(geoInfo.isAvailable());
    }

    public void test_0_x_0() {
        final String latitude = "0";
        final String lonitude = "x";
        final String distance = "10";

        final GeoInfo geoInfo = create(latitude, lonitude, distance);
        assertFalse(geoInfo.isAvailable());
    }

    private GeoInfo create(final String latitude, final String longitude, final String distance) {
        final GeoInfo geoInfo = new GeoInfo();
        geoInfo.latitude = latitude;
        geoInfo.longitude = longitude;
        geoInfo.distance = distance;
        return geoInfo;
    }

}
