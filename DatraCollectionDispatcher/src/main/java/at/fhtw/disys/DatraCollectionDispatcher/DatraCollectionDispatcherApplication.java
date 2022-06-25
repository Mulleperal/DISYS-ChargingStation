package at.fhtw.disys.DatraCollectionDispatcher;

import at.fhtw.disys.DatraCollectionDispatcher.models.JobInfo;
import at.fhtw.disys.DatraCollectionDispatcher.queue.communication.Producer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@SpringBootApplication
public class DatraCollectionDispatcherApplication {

	public static void main(String[] args) {
		SpringApplication.run(DatraCollectionDispatcherApplication.class, args);
	}



}
