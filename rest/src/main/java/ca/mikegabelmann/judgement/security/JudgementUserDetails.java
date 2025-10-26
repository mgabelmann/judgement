package ca.mikegabelmann.judgement.security;

import ca.mikegabelmann.judgement.codes.AccountStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;


public class JudgementUserDetails implements UserDetails, Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final String username;
    private String password;
    private String salt;
    private final AccountStatus accountStatus;
    private final Set<JudgementGrantedAuthority> authorities;


    /**
     * Constructor.
     * @param username
     * @param password
     * @param accountStatus
     * @param authorities
     */
    public JudgementUserDetails(String username, String password, String salt, AccountStatus accountStatus, List<JudgementGrantedAuthority> authorities) {
        this.username = username;
        this.password = password;
        this.salt = salt;
        this.accountStatus = accountStatus;
        this.authorities = new TreeSet<>(authorities);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.copyOf(authorities);
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return switch (accountStatus) {
            case NEW, PENDING, ACTIVE, INACTIVE, PASSWORD_RESET -> true;
            default -> false;
        };
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return !accountStatus.equals(AccountStatus.PASSWORD_RESET);
    }

    @Override
    public boolean isEnabled() {
        return switch (accountStatus) {
            case NEW, PENDING, ACTIVE, INACTIVE, PASSWORD_RESET -> true;
            default -> false;
        };

    }

    @Override
    public String toString() {
        return "JudgementUserDetails{" +
                "accountStatus=" + accountStatus.name() +
                ", username='" + username + '\'' +
                ", password='XXX'" +
                ", salt='YYY'" +
                ", authorities=" + authorities +
                '}';
    }

}
