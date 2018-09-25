package it.uniroma2.sdccproject.configuration;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// L'annotazione @Configuration fa sì che questa classe sia uno Spring Bean in grado di fornire configurazioni extra
// a Spring. Spring Boot, infatti, è completamente pre-configurato con impostazioni di default, ma lo sviluppatore
// può sovrascrivere tali configurazioni. La sovrascrittura avviene in due passi: 1) marcatura della classe che contiene
// le nuove configurazioni con l'annotazione @Configuration. 2) Definizione di Spring Bean che vanno a sovrascrivere
// le configurazioni di default
@Configuration
public class JacksonConfigurator {

    // La libreria Jackson è configurata di default in Spring per effettuare la serializzazione e deserializzazione JSON.
    // Jackson ha un ObjectMapper, che a sua volta ha una configurazione di default. Spring utilizza quello, a meno di
    // trovare una differente configurazione. Con il metodo di seguito viene costruito e configurato un ObjectMapper.
    // Spring utilizzerà questo metodo per costruire gli ObjectMapper necessari per la serializzazione e
    // deserializzazione dei dati in/da JSON,
    @Bean
    public ObjectMapper getObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        // Per intervenire nel processo di serializzazione per tutte le classi, si può applicare una configurazione a
        // livello di Object. In tal modo, tutte le classi che ereditano da Object (ovvero tutte le classi Java) verranno
        // serializzate applicando la configurazione di serializzazione definita come secondo parametro
        mapper.addMixIn(Object.class, IgnoreHibernatePropertiesInJackson.class);
        return mapper;
    }

    // La configurazione di Jackson che ci interessa è quella che consente di ignorare alcuni attributi sulla base del
    // loro nome. Infatti, Hibernate crea delle sottoclassi delle classi @Entity, aggiungendo degli attributi necessari
    // per la gestione della persistenza. Tali attributi si chiamano "hibernateLazyInitializer" e "handler" e non hanno
    // un significato a livello di business, dunque non ha senso serializzarli su rete per fornirli come risposta al
    // client. Per configurare Jackson in modo da non serializzarli è sufficiente creare una classe (anche vuota) e
    // annotarla con @JsonIgnorProperties({array dei nomi degli attributi da ignorare})
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private abstract class IgnoreHibernatePropertiesInJackson {
    }
}
