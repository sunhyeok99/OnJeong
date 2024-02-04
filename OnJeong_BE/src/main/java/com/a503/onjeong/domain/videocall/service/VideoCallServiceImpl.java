package com.a503.onjeong.domain.videocall.service;

import com.a503.onjeong.domain.videocall.dto.SessionIdRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.openvidu.java.client.*;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class VideoCallServiceImpl implements VideoCallService {
    @Value("${openvidu.url}")
    private String OPENVIDU_URL;

    @Value("${openvidu.secret}")
    private String OPENVIDU_SECRET;

    private OpenVidu openvidu;

    @PostConstruct
    public void init() {
        this.openvidu = new OpenVidu(OPENVIDU_URL, OPENVIDU_SECRET);
    }

    @Override
    public String initializeSession(SessionIdRequestDto sessionIdRequestDto) throws OpenViduJavaClientException, OpenViduHttpException {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> param = mapper.convertValue(sessionIdRequestDto, Map.class);
        SessionProperties properties = SessionProperties.fromJson(param).build();
        Session session = openvidu.createSession(properties);

        System.out.println("session is " + session);

        return session.getSessionId();
    }

    @Override
    public Connection createConnection(String sessionId) throws OpenViduJavaClientException, OpenViduHttpException {
        Session session = openvidu.getActiveSession(sessionId);
        if (session == null) {
            return null;
        }
        ConnectionProperties properties = ConnectionProperties.fromJson(null).build();
        return session.createConnection(properties);
    }
}
