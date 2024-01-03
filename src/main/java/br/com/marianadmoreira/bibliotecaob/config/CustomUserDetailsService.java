package br.com.marianadmoreira.bibliotecaob.config;





import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.marianadmoreira.bibliotecaob.model.UserModel;
import br.com.marianadmoreira.bibliotecaob.repository.IUserRepository;
import lombok.RequiredArgsConstructor;

@Service("securityUserDetailsService")
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {


    @Autowired
    private final IUserRepository userRepository;
    

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserModel user = userRepository.findByUsername(username);

        if(user == null)
            throw new UsernameNotFoundException(username);
            
        System.out.println("User loaded: " + user.getUsername());

        return new CustomUserDetails(user);
    }

}
