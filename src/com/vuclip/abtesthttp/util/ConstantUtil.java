package com.vuclip.abtesthttp.util;

import java.util.HashMap;
import java.util.Map;

public class ConstantUtil {

	public static final int role_admin = 1;
	public static final int role_editor = 2;
	public static final int role_programmer = 3;
	public static final int role_partner = 4;
	public static final int role_preeditor = 5;
	
	public static final int source_feed = 0 ; 
	public static final int source_fileupload = 1;
	public static final int source_yt = 2; 
	
	public static final int channel_status_new = 0 ; 
	public static final int channel_status_published = 1 ; 
	public static final int channel_status_disactive = 2 ; 
	
	public static final int video_status_new = 0 ; 
	public static final int video_status_published = 1 ; 
	public static final int video_status_disactive = 2 ; 
	
	//0: Failed due to connection lost; 1: Video missing;  2: Thumbnail missing; 3: Meta Data missing;  4: QC Failed; 6: successful ingestion(published); 7: New Reports
	public static final int activity_feed_resume_uploading = 0 ;
	public static final int activity_feed_video_missing = 1 ;
	public static final int activity_feed_thumbnail_missing = 2 ;
	public static final int activity_feed_metadata_missing = 3 ;
	public static final int activity_feed_qc_failed = 4 ;
	public static final int activity_feed_qc_passed = 6 ;
	public static final int activity_feed_new_reports = 7 ;
	
	public static final String FROM = "IngestionToolTask"; // the value only be used by IngestionToolTask.java to update the checkTimes
	public static final String COOKIE_DOMAIN_NAME = "vuclip.com";
	public static final String WEB_KEY = "content_track";
	public static final long COOKIE_MAX_AGE = 60 * 60 * 24 * 7 * 2;
	public static final int COOKIE_MAX_ACTIVE = 60 * 60 * 24 * 365 * 2;
	
	public static final String CT_ALL_GENRES_REFRESH = "ct_all_genres_refresh";
	
	public static final Map<String, Integer> attributes = new HashMap<String, Integer>();
	
	static {
		attributes.put("episode_no", 7);				attributes.put("geo_relevance", 8);
		attributes.put("age_group", 9);					attributes.put("gender_relevance", 10);				attributes.put("synopsis_description", 11);		attributes.put("actor", 12);
		attributes.put("actress", 13);					attributes.put("director", 14);						attributes.put("music_driector", 15);			attributes.put("singer", 16);
		attributes.put("performer", 17);				attributes.put("year_of_release", 18);				attributes.put("producer", 19);					attributes.put("writer", 20);
		attributes.put("mood", 21);						attributes.put("lyricist", 22);						attributes.put("broadcaster", 23);				attributes.put("god", 24);
		attributes.put("festival_event", 25);			attributes.put("temple", 26);						attributes.put("editor_rating", 27);			attributes.put("adult_nonadult", 31);
		attributes.put("adult_explicit_lyrics", 32);	attributes.put("adult_explicit_visual", 33);		attributes.put("adult_rating", 34);				attributes.put("movie_album_show_name", 47);		
		attributes.put("language", 49);					attributes.put("sub_provider", 3);
    }
	
	public static final String DAILY = "daily";
	public static final String WEEKLY = "weekly";
	public static final String MONTHLY = "monthly";
	
}
