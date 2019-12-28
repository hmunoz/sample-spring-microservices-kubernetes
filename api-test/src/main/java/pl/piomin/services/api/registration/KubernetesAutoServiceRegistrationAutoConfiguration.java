package pl.piomin.services.api.registration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.client.serviceregistry.AutoServiceRegistrationConfiguration;
import org.springframework.cloud.client.serviceregistry.AutoServiceRegistrationProperties;
import org.springframework.cloud.kubernetes.PodUtils;
import org.springframework.cloud.kubernetes.discovery.KubernetesDiscoveryProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.UnknownHostException;

@Configuration
//@ConditionalOnBean(AutoServiceRegistrationProperties.class)
@ConditionalOnProperty(name = "spring.cloud.kubernetes.discovery.register", havingValue = "true")
@AutoConfigureAfter({ AutoServiceRegistrationConfiguration.class, KubernetesServiceRegistryAutoConfiguration.class })
public class KubernetesAutoServiceRegistrationAutoConfiguration {

	@Autowired
	AutoServiceRegistrationProperties autoServiceRegistrationProperties;

	@Bean
	@ConditionalOnMissingBean
	public KubernetesAutoServiceRegistration autoServiceRegistration(
			@Qualifier("serviceRegistry") KubernetesServiceRegistry registry,
			AutoServiceRegistrationProperties autoServiceRegistrationProperties,
			KubernetesDiscoveryProperties properties,
			PodUtils podUtils) {
		return new KubernetesAutoServiceRegistration(registry,
				autoServiceRegistrationProperties, properties, podUtils);
	}

	@Bean
	public KubernetesAutoServiceRegistrationListener listener(KubernetesAutoServiceRegistration registration) {
		return new KubernetesAutoServiceRegistrationListener(registration);
	}

	@Bean
	public KubernetesAutoRegistration registration(KubernetesDiscoveryProperties properties, ApplicationContext context) throws UnknownHostException {
		return KubernetesAutoRegistration.registration(properties, context);
	}

}
