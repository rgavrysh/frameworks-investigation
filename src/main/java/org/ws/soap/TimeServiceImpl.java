package org.ws.soap;

import javax.jws.WebService;
import java.time.Instant;

@WebService(endpointInterface = "org.ws.soap.TimeService")
public class TimeServiceImpl implements TimeService {
    @Override
    public String getServerTime() {
        return Instant.now().toString();
    }
}
