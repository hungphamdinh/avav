package com.example.callvideo.Model.LoadCourse;

import java.util.HashMap;

interface ILoadCourse {
     void loadDetailCourse(String courseId,HashMap<String,Object> tutorMap, HashMap<String,Object> courseMap) ;
     void loadDetailTutor(String tutorId,HashMap<String,Object>tutorMap);
}
