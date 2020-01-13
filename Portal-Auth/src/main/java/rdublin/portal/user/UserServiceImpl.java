package rdublin.portal.user;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rdublin.portal.privelege.Privilege;
import rdublin.portal.privelege.Role;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Transactional
@Service(value = "userService")
public class UserServiceImpl implements UserDetailsService, UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bcryptEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findFirstByName(username);
        if (user == null) {
            throw new UsernameNotFoundException("Invalid username or password.");
        }

        return new org.springframework.security.core.userdetails.User(
                user.getName(), user.getPassword(), true, true, true,
                true, getAuthorities(user.getRoles()));
    }

    @Override
    @Transactional
    public Set<Privilege> getPrivileges(@NotNull Set<Role> roles) {
        return roles.stream().flatMap(r -> r.getPrivileges().stream()).collect(Collectors.toSet());
    }

    @Transactional
    public Set<GrantedAuthority> getAuthorities(@NotNull Set<Role> roles) {
        return Stream.concat(
                roles.stream().map(p -> p.getName()),
                getPrivileges(roles).stream().map(p -> p.getName())
        )
                     .map(p -> new SimpleGrantedAuthority(p))
                     .collect(Collectors.toSet());
    }

    @Override
    public List findAll() {
        List<User> list = new ArrayList<>();
        userRepository.findAll().iterator().forEachRemaining(list::add);
        return list;
    }

    @Override
    public void delete(int id) {
        userRepository.deleteById(id);
    }

    @Override
    public User findOne(String username) {
        return userRepository.findFirstByName(username);
    }

    @Override
    public User findById(int id) {
        Optional<User> optionalUser = userRepository.findById(id);
        return optionalUser.isPresent() ? optionalUser.get() : null;
    }

    @Override
    public UserDto update(UserDto userDto) {
        User user = findById(userDto.getId());
        if (user != null) {
            BeanUtils.copyProperties(userDto, user, "password");
            if (userDto.getPassword() != null) {
                user.setPassword(bcryptEncoder.encode(user.getPassword()));
            }
            userRepository.save(user);
        }
        return userDto;
    }

    @Override
    public User save(UserDto user) {
        User newUser =
                User.builder().name(user.getName()).email(user.getEmail())
                    .dob(user.getDob()).gender(user.getGender())
                    .password(bcryptEncoder.encode(user.getPassword())).build();
        return userRepository.save(newUser);
    }
}
