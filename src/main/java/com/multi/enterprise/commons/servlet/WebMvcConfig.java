package com.multi.enterprise.commons.servlet;

import java.util.List;

import javax.xml.transform.Source;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.ResourceHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.support.AllEncompassingFormHttpMessageConverter;
import org.springframework.http.converter.xml.Jaxb2RootElementHttpMessageConverter;
import org.springframework.http.converter.xml.SourceHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.multi.enterprise.commons.jackson.ObjectMapperMixinInitializer;
import com.multi.enterprise.commons.jackson.ObjectMapperUtils;
import com.multi.enterprise.commons.jaxb.Jaxb2MessageConverter;

@Configuration
@EnableWebMvc
public class WebMvcConfig extends WebMvcConfigurerAdapter{

	@Autowired(required= false)
	protected ObjectMapperMixinInitializer mixinInitializer;
	
	@Override
	public void addResourceHandlers(final ResourceHandlerRegistry registry){
		registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
	}
	
	public MappingJackson2HttpMessageConverter jsonMessageConverter(){
		return new MappingJackson2HttpMessageConverter( this.jacksonObjectMapper());
	}
	
	public ObjectMapper jacksonObjectMapper(){
		return ObjectMapperUtils.createWithTypeResolver(null, this.mixinInitializer);
	}
	
	public Jaxb2RootElementHttpMessageConverter xmlMessageConverter(){
		return new Jaxb2MessageConverter();
	}
	
	@Override
	public void configureMessageConverters( final List<HttpMessageConverter<?>> converters){
		converters.add(new ByteArrayHttpMessageConverter());
		converters.add( new StringHttpMessageConverter());
		converters.add( new ResourceHttpMessageConverter());
		converters.add( new SourceHttpMessageConverter<Source>());
		converters.add( new AllEncompassingFormHttpMessageConverter());
		
		final Jaxb2RootElementHttpMessageConverter xmlConverter = this.xmlMessageConverter();
		if(xmlConverter != null ){
			converters.add(xmlConverter);
		}
		
		final MappingJackson2HttpMessageConverter jsonConverter = this.jsonMessageConverter();
		if(jsonConverter != null){
			converters.add(jsonConverter);
		}
		
	}
	
	
}
