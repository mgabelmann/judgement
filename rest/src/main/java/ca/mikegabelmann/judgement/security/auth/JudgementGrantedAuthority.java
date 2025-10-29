package ca.mikegabelmann.judgement.security.auth;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.Assert;

import java.io.Serial;
import java.io.Serializable;


public class JudgementGrantedAuthority implements GrantedAuthority, Comparable<JudgementGrantedAuthority>, Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final String role;


    /**
     * Constructor.
     * @param role role or authority
     */
    public JudgementGrantedAuthority(final String role) {
        Assert.hasText(role, "role must not be empty");
        this.role = role;
    }

    @Override
    public String getAuthority() {
        return this.role;
    }

    @Override
    public int compareTo(JudgementGrantedAuthority o) {
        return this.role.compareTo(o.role);
    }

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof JudgementGrantedAuthority that)) return false;
        return role.equals(that.role);
    }

    @Override
    public int hashCode() {
        return role.hashCode();
    }

    @Override
    public String toString() {
        return this.role;
    }


    public static JudgementGrantedAuthority createRole(final String role) {
        Assert.hasText(role, "role must not be empty");
        return new JudgementGrantedAuthority("ROLE_" + role);
    }

    public static JudgementGrantedAuthority createProjectAuthority(final String projectName, final String projectRole) {
        Assert.hasText(projectName, "projectName must not be empty");
        Assert.hasText(projectRole, "projectRole must not be empty");

        return new JudgementGrantedAuthority(projectRole + ":" + projectName);
    }

    public static JudgementGrantedAuthority createPermission(final String permission) {
        Assert.hasText(permission, "permission must not be empty");
        return new JudgementGrantedAuthority(permission);
    }

}
