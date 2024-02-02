package com.a503.onjeong.domain.videocall.controller;

import com.a503.onjeong.domain.videocall.dto.SessionIdRequestDto;
import io.openvidu.java.client.OpenViduHttpException;
import io.openvidu.java.client.OpenViduJavaClientException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

public interface VideoCallController {
    ResponseEntity<String> initializeSession(@RequestBody SessionIdRequestDto sessionIdRequestDto)
            throws OpenViduJavaClientException, OpenViduHttpException;

//    ResponseEntity<String> createConnection(@PathVariable("sessionId") String sessionId,
//                                            @RequestBody(required = false) Map<String, Object> params)
//            throws OpenViduJavaClientException, OpenViduHttpException;

    //    @Override
    //    @PostMapping("/sessions/{sessionId}")
    //    public ResponseEntity<String> createConnection(@PathVariable("sessionId") String sessionId,
    //                                                   @RequestBody(required = false) Map<String, Object> params)
    //            throws OpenViduJavaClientException, OpenViduHttpException {
    //        Session session = openvidu.getActiveSession(sessionId);
    //        if (session == null) {
    //            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    //        }
    //        ConnectionProperties properties = ConnectionProperties.fromJson(params).build();
    //        Connection connection = session.createConnection(properties);
    //
    //        return new ResponseEntity<>(connection.getToken(), HttpStatus.OK);
    //    }
    @GetMapping("sessions/{sessionId}")
    ResponseEntity<String> createConnection(@PathVariable("sessionId") String sessionId) throws OpenViduJavaClientException, OpenViduHttpException;
}
