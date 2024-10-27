package com.petstore.testcases;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.github.javafaker.Faker;
import com.petstore.endpoints.UserEndPoints;
import com.petstore.payload.User;

import io.restassured.response.Response;
import junit.framework.Assert;

public class UserTests 
{
	Faker faker;
	User userPayload;
	public Logger logger;
	
	@BeforeClass
	public void setUpData()
	{
		faker = new Faker();
		userPayload = new User();
		
		userPayload.setId(faker.idNumber().hashCode());
		userPayload.setUserName(faker.name().username());
		userPayload.setFirstName(faker.name().firstName());
		userPayload.setLastName(faker.name().lastName());
		userPayload.setEmail(faker.internet().safeEmailAddress());
		userPayload.setPassword(faker.internet().password(5,10));
		userPayload.setPhone(faker.phoneNumber().cellPhone());
		
		logger = LogManager.getLogger(this.getClass());
		
	}
	
	@Test(priority = 1)
	public void testPostUser()
	{
		logger.info("*********** Creating User ************");
		Response response = UserEndPoints.createUser(userPayload);
		response.then().log().all();
		Assert.assertEquals(response.getStatusCode(), 200);
		logger.info("*********** User is Created ************");
	}
	
	@Test(priority=2)
	public void testGetUserByName()
	{
		logger.info("******** Reading User info ********");
		Response response = UserEndPoints.readUser(this.userPayload.getUserName());
		response.then().log().all();
		Assert.assertEquals(response.getStatusCode(), 200);
		logger.info("******** User info is Displayed ********");
	}
	
	@Test(priority=3)
	public void testUpdateUserByName()
	{
		logger.info("********* Updating User *********");
		
		userPayload.setFirstName(faker.name().firstName());
		userPayload.setLastName(faker.name().lastName());
		userPayload.setEmail(faker.internet().safeEmailAddress());
		
		Response response = UserEndPoints.updateUser(this.userPayload.getUserName(), userPayload);
		response.then().log().body();
		response.then().log().all();
		Assert.assertEquals(response.getStatusCode(), 200);
		
		logger.info("********* User is Updated *********");
		
		Response responseAfterUpdate = UserEndPoints.readUser(this.userPayload.getUserName());
		Assert.assertEquals(responseAfterUpdate.getStatusCode(), 200);
		
	}
	
	@Test(priority=4)
	public void testDeleteUserByName()
	{
		logger.info("******** Deleting  User ********");
		Response response = UserEndPoints.deleteUser(this.userPayload.getUserName());
		Assert.assertEquals(response.getStatusCode(), 200);
		logger.info("******** User is Deleted ********");
	}

}
