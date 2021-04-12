package com.kog.spr.model;

import lombok.Data;

/**
 * Photo Value Object class to hold Photo Information from Random API url.
 */
@Data
public class PhotoVO {
    private Integer albumId;
    private Integer id;
    private String title;
    private String url;
    private String thumbnail;
}
