
package com.bridgeit.DiscoveryEurekaNoteService.utilservice;

import java.util.Optional;

import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import com.bridgeit.DiscoveryEurekaNoteService.utilservice.Exception.TodoException;


/**
 * @author LAKSHMI PRIYA
 * @since DATE:10-07-2018
 *        <p>
 *        <b>RestPreConditions to check for null.</b>
 *        </p>
 */
@Component
public class RestPreCondition {
	 private RestPreCondition() {
	    }
	 public static  <T> Optional<T> checkArgument(Optional<T> checkArgument,String errorMessage) throws TodoException {
	        if (!checkArgument.isPresent()) {
	            throw new TodoException(String.valueOf(errorMessage));
	        }
	        return checkArgument;
	    }
	 /**
	 * @param reference
	 * @param errorMessage
	 * @return
	 * @throws TodoException
	 */
	public  <T> T checkNotNull(T reference, @Nullable Object errorMessage) throws TodoException {
	        if (reference == null||reference=="") {
	            throw new TodoException(String.valueOf(errorMessage));
	        }
	        return reference;
	    }
	/**
	 * @param checkArgument
	 * @param errorMessage
	 * @return
	 * @throws TodoException
	 */
	public  <T> boolean checkArgument(boolean checkArgument, @Nullable Object errorMessage) throws TodoException {
        if (!checkArgument) {
            throw new TodoException(String.valueOf(errorMessage));
        }
        return checkArgument;
   
	}

}
