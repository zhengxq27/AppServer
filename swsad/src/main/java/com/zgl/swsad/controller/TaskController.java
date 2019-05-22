package com.zgl.swsad.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.zgl.swsad.authorization.annotation.Authorization;
import com.zgl.swsad.authorization.annotation.CurrentUser;
import com.zgl.swsad.model.*;
import com.zgl.swsad.service.*;
import com.zgl.swsad.util.ReturnMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RestController
public class TaskController {

    @Autowired
    private TaskService taskService;
    @Autowired
    private QuestionareService questionareService;
    @Autowired
    private QuestionService questionService;
    @Autowired
    private ErrandService errandService;

    //创建问卷型任务
    @RequestMapping(value = "/tasks/questionares",method = RequestMethod.POST)
    public Object create_questionare_task(@RequestBody JSONObject param){
        JSONObject task_json = param.getJSONObject("task");
        JSONObject questionare_json = param.getJSONObject("questionare");
        JSONArray questions_json = questionare_json.getJSONArray("questions");
        int count = 0;
        int num = 2 + questionare_json.size() - 3;
        //注意这里的questionare_size得到的长度不是以JsonObject作为单位，而是以键值对作为单位,所以还要加上前面的3个键值对

        Task task = (Task)JSONObject.toJavaObject(task_json,Task.class);
        int opNum1 = taskService.insertTask(task);
        if(opNum1 == 1){
            count++;
        }

        Questionare questionare = (Questionare)JSONObject.toJavaObject(questionare_json,Questionare.class);
       int opNum2 = questionareService.insertQuestionare(questionare);
       if( opNum2 == 1){
            count++;
        }

        for(int i = 0; i < questions_json.size(); i++){
            JSONObject question_json = (JSONObject)questions_json.getJSONObject(i); //这里不能是get(i),get(i)只会得到键值对
            Question question = (Question)JSONObject.toJavaObject(question_json,Question.class);
            int opNum3 = questionService.insertQuestion(question);
            if( opNum3 == 1 ){
                count++;
            }
        }
        System.out.println("数组长度" + questions_json.size());
        System.out.println("num" + questionare_json.size());

        if( count == num){
            return new ResponseEntity(new ReturnMsg("create task successfully!"), HttpStatus.OK);
       }
        return new ResponseEntity(new ReturnMsg("server error"),HttpStatus.INTERNAL_SERVER_ERROR);
        //return questions_json.toJSONString();
    }

    //创建跑腿型任务
    @RequestMapping(value = "/tasks/errands", method = RequestMethod.POST)
    public Object create_errand_task(@RequestBody JSONObject param){

        JSONObject task_json = param.getJSONObject("task");
        JSONObject errand_json = param.getJSONObject("errand");
        int count = 0;

        Task task = (Task)JSONObject.toJavaObject(task_json,Task.class);
        int opNum1 = taskService.insertTask(task);
        if( opNum1 == 1 ){
            count++;
        }

        Errand errand = (Errand)JSONObject.toJavaObject(errand_json,Errand.class);
        int opNum2 = errandService.insertErrand(errand);
        if( opNum2 == 1 ){
            count++;
        }

        if( count == 2 ){
            return new ResponseEntity(new ReturnMsg("create task successfully!"), HttpStatus.OK);
        }
        return new ResponseEntity(new ReturnMsg("server error"),HttpStatus.INTERNAL_SERVER_ERROR);

    }

    //通过id查找task
    @CrossOrigin
    //@Authorization
    @RequestMapping(value="/tasks/{taskID}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Object> queryTask (@PathVariable int taskID){
        Task task = taskService.selectTask(taskID);
        if(task == null)
            return new ResponseEntity(new ReturnMsg("invalid taskId"), HttpStatus.NOT_FOUND);

        return new ResponseEntity(task,HttpStatus.NOT_FOUND);
    }

    //通过id修改task
    @CrossOrigin
    //@Authorization
    @RequestMapping(value = "/tasks/{taskId}",method = RequestMethod.PUT,consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Object modifyTask(@RequestBody Task task, @CurrentUser User currentUser)
    {
        //System.out.println(currentUser.getUserId());
        //System.out.println(task.getPubUserId());
        if(currentUser.getUserId() == task.getPubUserId()){
            int opNum = taskService.updateTask(task);
            if(opNum == 1)
            {
                return new ResponseEntity(new ReturnMsg("modify task success"),HttpStatus.OK);
            }
            return new ResponseEntity(new ReturnMsg("server error"),HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity(new ReturnMsg("invalid, this is not your task"),HttpStatus.BAD_REQUEST);
    }

    //通过id删除task
    @CrossOrigin
    @Authorization
    @RequestMapping(value = "/tasks/{taskID}",method = RequestMethod.DELETE,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Object deleteTask (@PathVariable int taskID )
    {

        int opNum =  taskService.deleteTask(taskID);
        if(opNum == 1)
        {
            return new ResponseEntity(new ReturnMsg("delete success"),HttpStatus.OK);
        }
        else return new ResponseEntity(new ReturnMsg("server error, delete fail"),HttpStatus.INTERNAL_SERVER_ERROR);


    }

    //根据taskId查找问卷
    @CrossOrigin
    @Authorization
    @RequestMapping(value = "/tasks/{taskID}/questionares", method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String getQuestionareByTaskId(@PathVariable int taskID){

        Questionare questionare = questionareService.selectQuestionareByTaskID(taskID);
        String questionare_str = JSONObject.toJSONString(questionare);
        int questionnareId = questionare.getQuestionareId();
        ArrayList<Question> questions = questionService.selectQuestionByQuestionareID(questionnareId); //获取问题列表
        String questions_str = JSONObject.toJSONString(questions);
        String str = "questions: " + questions_str;
        StringBuilder temp = new StringBuilder(questionare_str);
        temp.insert(questionare_str.length()-2,str);
        String Str = temp.toString();
        return Str;

    }

    //根据taskId修改问卷
    @CrossOrigin
    //@Authorization
    @RequestMapping(value = "/tasks/{taskID}/questionares", method = RequestMethod.PUT,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Object modifyQuestionare(@RequestBody Questionare questionare,@CurrentUser User currentUser){
        int taskId = questionare.getTaskId();
        int pubTaskUserId = taskService.selectTask(taskId).getPubUserId();
        if(pubTaskUserId == currentUser.getUserId()){
            int opNum = questionareService.updateQuestionare(questionare);
            if(opNum == 1){
                return new ResponseEntity(new ReturnMsg("modify questionare success"),HttpStatus.OK);
            }
            return new ResponseEntity(new ReturnMsg("server error"),HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity(new ReturnMsg("invalid, this is not your task so you can't modify the questionare.")
                ,HttpStatus.BAD_REQUEST);
    }


    //通过id查找errand
    @CrossOrigin
    //@Authorization
    @RequestMapping(value = "/tasks/{taskID}/errands",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Object getErrandByTaskId(@PathVariable int taskID){

        Errand errand = errandService.selectErrand(taskID);
        if(errand == null)
            return new ResponseEntity(new ReturnMsg("invalid taskId"), HttpStatus.NOT_FOUND);

        return new ResponseEntity(errand,HttpStatus.OK);
    }

    //根据taskid修改errand
    @CrossOrigin
    //@Authorization
    @RequestMapping(value = "/tasks/{taskID}/errands",method = RequestMethod.PUT,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Object modifyErrand(@RequestBody Errand errand, @CurrentUser User currentUser){
        int taskId = errand.getTaskId();
        int pubTaskUserId = taskService.selectTask(taskId).getPubUserId();
        if( pubTaskUserId == currentUser.getUserId())
        {
            int opNum = errandService.updateErrand(errand);
            if( opNum == 1 ){
                return new ResponseEntity(new ReturnMsg("modify errand success"),HttpStatus.OK);
            }
            return new ResponseEntity(new ReturnMsg("server error"),HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity(new ReturnMsg("invalid, this is not your task so you can't modify the errand.")
                ,HttpStatus.BAD_REQUEST);
    }




}
