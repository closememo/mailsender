package com.closememo.mailsender.controller.system;

import com.closememo.mailsender.config.openapi.apitags.SystemApiTag;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SystemApiTag
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@PreAuthorize("hasRole('SYSTEM')")
@RequestMapping("/mail-sender/system")
@RestController
public @interface SystemMailSenderInterface {

}
