package ca.mikegabelmann.judgement.security;

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

    /**
     * Constructor.
     * @param projectName project name
     * @param projectRole project role
     */
    public JudgementGrantedAuthority(final String projectName, final String projectRole) {
        Assert.hasText(projectName, "projectName must not be empty");
        Assert.hasText(projectRole, "projectRole must not be empty");

        this.role = projectName + ":" + projectRole;
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
    public int hashCode() {
        return this.role.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return this.role.equals(((JudgementGrantedAuthority) obj).role);
    }

    @Override
    public String toString() {
        return this.role;
    }

}
