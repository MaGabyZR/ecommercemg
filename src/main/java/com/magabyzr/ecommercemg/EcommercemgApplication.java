package com.magabyzr.ecommercemg;

import com.magabyzr.ecommercemg.services.UserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class EcommercemgApplication {
    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(EcommercemgApplication.class, args);
        var service = context.getBean(UserService.class);
        service.fetchPaginatedProducts(0, 10);
        //service.fetchSortedProducts();
        //service.fetchProductsBySpecifications("prod", null, null);
        //service.fetchProductsByCriteria();
        //service.printLoyalProfiles();
        //service.fetchProducts();
        //service.fetchUser();


/*        var user = User.builder()
                .name("Daniel")
                .password("1234")
                .email("daniel@gmail.com")
                .build();

        repository.save(user);*/

       /* var address = Address.builder()
                .street("123 Main St")
                .city("Main St")
                .state("Main St")
                .zip("1234")
                .build();

        user.addAddress(address);*/

        //user.addTag("tag1");

/*        var profile = Profile.builder()
                        .bio("Daniel blah blah blah")
                                .build();

        user.setProfile(profile);
        profile.setUser(user);

        System.out.println(user);*/


    }
}
