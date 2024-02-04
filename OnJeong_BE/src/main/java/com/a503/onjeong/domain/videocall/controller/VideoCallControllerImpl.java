package com.a503.onjeong.domain.videocall.controller;

import com.a503.onjeong.domain.videocall.dto.SessionIdRequestDto;
import com.a503.onjeong.domain.videocall.service.VideoCallService;
import io.openvidu.java.client.Connection;
import io.openvidu.java.client.OpenViduHttpException;
import io.openvidu.java.client.OpenViduJavaClientException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/video-call")
@RequiredArgsConstructor
public class VideoCallControllerImpl implements VideoCallController {

    private final VideoCallService videoCallService;

//    @Value("${openvidu.url}")
//    private String OPENVIDU_URL;
//
//    @Value("${openvidu.secret}")
//    private String OPENVIDU_SECRET;
//
//    private OpenVidu openvidu;
//
//    @PostConstruct
//    public void init() {
//        this.openvidu = new OpenVidu(OPENVIDU_URL, OPENVIDU_SECRET);
//    }

    @Override
    @PostMapping("/sessions")
    public ResponseEntity<String> initializeSession(@RequestBody SessionIdRequestDto sessionIdRequestDto)
            throws OpenViduJavaClientException, OpenViduHttpException {
        String sessionId = videoCallService.initializeSession(sessionIdRequestDto);
        System.out.println("session id is " + sessionId);
        return new ResponseEntity<>(sessionId, HttpStatus.OK);
    }

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
    @Override
    @GetMapping("sessions/{sessionId}")
    public ResponseEntity<String> createConnection(@PathVariable("sessionId") String sessionId) throws OpenViduJavaClientException, OpenViduHttpException {

        System.out.println("received session is " + sessionId);
        Connection connection = videoCallService.createConnection(sessionId);
        System.out.println("connection is " + connection);
        if (connection == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(connection.getToken(), HttpStatus.OK);
    }
}
