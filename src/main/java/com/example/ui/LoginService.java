package com.example.ui;

import java.io.Serializable;

import org.atmosphere.config.service.Singleton;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.function.RenderingResponse;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.vaadin.flow.component.cookieconsent.CookieConsent.Position;
import com.vaadin.flow.component.notification.Notification;


@Service
@Singleton
public class LoginService implements Serializable {

	private static boolean loggedIn = false;
	private String name;
	private String password;
	private String hostName;
	public Boolean login(String name, String password) {
		this.name = name;
		this.password = password;
		if (name == null || name.isEmpty() || password == null || password.isBlank()) {
			LoginService.loggedIn = false;
			return null;
		} else if (tryServer2Connect())
		{
			LoginService.loggedIn = true;
			return Boolean.TRUE;
		} else {
			LoginService.loggedIn = false;
			return Boolean.FALSE ;
		}}

	private boolean tryServer2Connect() {
		boolean success = false;
		Unirest.setTimeouts(0, 0);
		if (getHostName() != null)
			try {
				HttpResponse<String> response = Unirest
						.get(String.format("http://%s:80/REST/node", getHostName()))
						.basicAuth(getName(), getPassword())
						.header("Content-Type", "application/json")
						//.header("Authorization", "Basic YWRtaW46YWRtaW4=")
						.asString();
				Notification.show(response.getStatusText(), 2000, Notification.Position.MIDDLE);
				if (response.getStatus() == 200) {
					success = true;
				}
			} catch (UnirestException e) {
				e.printStackTrace();
			} 
		
		return success;
	}

	public static boolean isLoggedIn() {
		return LoginService.loggedIn;
	}

	public String getName() {
		return name;
	}

	public String getPassword() {
		return password;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}







}
