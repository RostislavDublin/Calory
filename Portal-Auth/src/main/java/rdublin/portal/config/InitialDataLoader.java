package rdublin.portal.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import rdublin.portal.auth.AuthClientDetails;
import rdublin.portal.auth.AuthClientRepository;
import rdublin.portal.privelege.Privilege;
import rdublin.portal.privelege.PrivilegeRepository;
import rdublin.portal.privelege.Role;
import rdublin.portal.privelege.RoleRepository;
import rdublin.portal.user.User;
import rdublin.portal.user.UserRepository;

import java.sql.Date;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

@Component
public class InitialDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(InitialDataLoader.class);

    @Autowired
    RoleRepository roleRepository;
    @Autowired
    PrivilegeRepository privilegeRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    AuthClientRepository authClientRepository;

    boolean alreadysetup = false;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {

        if (!alreadysetup) {
            LOGGER.info("INITIAL DATA LOAD :: STARTED");

            LOGGER.info("Create privileges");
            Privilege crudOwnUser = createPrivilegeIfNotFound("USER_OWN_CRUD_PRIVILEGE", "CRUD own user");
            Privilege crudOwnMeal = createPrivilegeIfNotFound("MEAL_OWN_CRUD_PRIVILEGE", "CRUD own meal");
            Privilege crudAllUser = createPrivilegeIfNotFound("USER_ALL_CRUD_PRIVILEGE", "CRUD all users");
            Privilege crudAllMeal = createPrivilegeIfNotFound("MEAL_ALL_CRUD_PRIVILEGE", "CRUD all meal");

            Set<Privilege> adminPrivileges = new HashSet<>(Arrays.asList(crudAllUser, crudAllMeal));
            Set<Privilege> userPrivileges = new HashSet<>(Arrays.asList(crudOwnUser, crudOwnMeal));
            Set<Privilege> managerPrivileges = new HashSet<>(Arrays.asList(crudAllUser));

            LOGGER.info("Create roles");
            Map<String, Role> roles = new HashMap<>();
            roles.put("adminRole",createRoleIfNotFound("ROLE_ADMIN", "CRUD all records and users", adminPrivileges));
            roles.put("managerRole", createRoleIfNotFound("ROLE_USER_MANAGER", "CRUD all users", managerPrivileges));
            roles.put("userRole", createRoleIfNotFound("ROLE_REGULAR_USER", "CRUD own profile and calories", userPrivileges));

            LOGGER.info("Create several pre-defined users");
            Stream.of("Admin|1971-01-01|Male|adminRole",
                    "Manager|1972-02-02|Female|managerRole",
                    "TestUser|1973-03-03|Male|userRole")
                  .forEach(userDefinition -> {
                      String[] userParsed = userDefinition.split("\\|");
                      User user = userRepository.findFirstByName(userParsed[0]);
                      if (user == null) {
                          LOGGER.info("User {} is absent and to be created", userParsed[0]);
                          user = User
                                  .builder().name(userParsed[0]).email(userParsed[0].toLowerCase() + "@user.com")
                                  .dob(Date.valueOf(userParsed[1])).gender(userParsed[2])
                                  .roles(new HashSet<>(Arrays.asList(roles.get(userParsed[3]))))
                                  .password(passwordEncoder.encode("password")).build();
                          userRepository.save(user);
                      } else if (!user.getRoles().contains(roles.get(userParsed[3]))) {
                          LOGGER.info("User {} exists, needs for USER role", userParsed[0]);
                          user.getRoles().add(roles.get(userParsed[3]));
                          userRepository.save(user);
                      }
                  });
            long userNumber = userRepository.count();
            LOGGER.info("Total user number: " + userNumber);

            LOGGER.info("Create Auth Clients");
            String authClientId = "browser";
            if (!authClientRepository.findByClientId(authClientId).isPresent()) {
                AuthClientDetails clientDetails = new AuthClientDetails();
                clientDetails.setClientId(authClientId);
                clientDetails.setClientSecret(passwordEncoder.encode("secret"));
                clientDetails.setScopes("ui");
                clientDetails.setGrantTypes("refresh_token,password");

                authClientRepository.save(clientDetails);
            }
            authClientId = "calories-service";
            if (!authClientRepository.findByClientId(authClientId).isPresent()) {
                AuthClientDetails clientDetails = new AuthClientDetails();
                clientDetails.setClientId(authClientId);
                clientDetails.setClientSecret(passwordEncoder.encode("secret"));
                clientDetails.setScopes("server");
                clientDetails.setGrantTypes("refresh_token,client_credentials");

                authClientRepository.save(clientDetails);
            }
            LOGGER.info("INITIAL DATA LOAD :: COMPLETED");
            alreadysetup = true;
        }
    }

    private Privilege createPrivilegeIfNotFound(String name, String description) {
        Privilege privilege = privilegeRepository.findByName(name);
        if (privilege == null) {
            privilege = Privilege.builder().name(name).description(description).build();
            privilegeRepository.save(privilege);
        }
        return privilege;
    }

    private Role createRoleIfNotFound(String name, String description, Set<Privilege> privileges) {

        Role role = roleRepository.findByName(name);
        if (role == null) {
            role = Role.builder().name(name).description(description).privileges(privileges).build();
            role.setPrivileges(privileges);
            roleRepository.save(role);
        }
        return role;
    }
}
