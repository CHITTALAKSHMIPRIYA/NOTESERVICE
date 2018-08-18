/**
 * 
 */
package com.bridgeit.DiscoveryEurekaNoteService.noteservice.repository;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author LAKSHMI PRIYA
 * @since DATE:10-07-2018
 *        <p>
 *        <b>A POJO class with the user details.</b>
 *        </p>
 */
@FeignClient(name="UserService",url="http://localhost:8762")
@Service
public interface IFeign {
	@GetMapping("user/displayuser")
	public List<?> getuser();

}
