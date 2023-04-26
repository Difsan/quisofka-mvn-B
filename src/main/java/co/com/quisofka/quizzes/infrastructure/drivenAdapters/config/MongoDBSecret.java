package co.com.quisofka.quizzes.infrastructure.drivenAdapters.config;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class MongoDBSecret {
    private final String uri;
    private final String host;
    private final int port;
    private final String database;

}
