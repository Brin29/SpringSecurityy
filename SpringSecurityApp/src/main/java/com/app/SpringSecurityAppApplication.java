package com.app;

import com.app.persistence.entity.PermissionEntity;
import com.app.persistence.entity.RoleEnum;
import com.app.persistence.entity.RolesEntity;
import com.app.persistence.entity.UserEntity;
import com.app.persistence.entity.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;
import java.util.Set;

@SpringBootApplication
public class SpringSecurityAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringSecurityAppApplication.class, args);
	}

	@Bean
	CommandLineRunner init(UserRepository userRepository){

		/*Cuando se incertan los usuarios
		tambien lo hacen los roles
		y a su vez los permisos por eso el cascadeType.ALL*/

		// Funcion Lambda
		/* CREATE PERMISION*/
		return args -> {
			PermissionEntity createPermission = PermissionEntity.builder()
					.name("CREATE")
					.build();

			PermissionEntity readPermission = PermissionEntity.builder()
					.name("READ")
					.build();

			PermissionEntity updatePermission = PermissionEntity.builder()
					.name("UPDATE")
					.build();

			PermissionEntity deletePermission = PermissionEntity.builder()
					.name("DELETE")
					.build();

			PermissionEntity refactorPermission = PermissionEntity.builder()
					.name("REFACTOR")
					.build();

			/* CREATE ROLES */
			RolesEntity roleAdmin = RolesEntity.builder()
					.roleEnum(RoleEnum.ADMIN)
					.permissionList(Set.of(createPermission, readPermission, updatePermission, deletePermission))
					.build();

			RolesEntity roleUser = RolesEntity.builder()
					.roleEnum(RoleEnum.USER)
					.permissionList(Set.of(createPermission, readPermission))
					.build();

			RolesEntity roleInvited = RolesEntity.builder()
					.roleEnum(RoleEnum.INVITED)
					.permissionList(Set.of(readPermission))
					.build();

			RolesEntity roleDeveloper = RolesEntity.builder()
					.roleEnum(RoleEnum.DEVELOPER)
					.permissionList(Set.of(createPermission, readPermission, updatePermission, deletePermission, refactorPermission))
					.build();

			/* CREATE USERS */
			UserEntity userSantiago = UserEntity.builder()
					.username("santiago")
					.password("1234")
					.isEnabled(true)
					.accountNoExpired(true)
					.accountNoLocked(true)
					.credentialsNoExpired(true)
					.rolesEntities(Set.of(roleAdmin))
					.build();

			UserEntity userDaniel = UserEntity.builder()
					.username("Daniel")
					.password("1234")
					.isEnabled(true)
					.accountNoExpired(true)
					.accountNoLocked(true)
					.credentialsNoExpired(true)
					.rolesEntities(Set.of(roleUser))
					.build();

			UserEntity userAndrea = UserEntity.builder()
					.username("andrea")
					.password("1234")
					.isEnabled(true)
					.accountNoExpired(true)
					.accountNoLocked(true)
					.credentialsNoExpired(true)
					.rolesEntities(Set.of(roleInvited))
					.build();

			UserEntity userBreiner = UserEntity.builder()
					.username("breiner")
					.password("1234")
					.isEnabled(true)
					.accountNoExpired(true)
					.accountNoLocked(true)
					.credentialsNoExpired(true)
					.rolesEntities(Set.of(roleDeveloper))
					.build();


			userRepository.saveAll(List.of(userSantiago, userBreiner, userAndrea, userDaniel));
		};
	}

}
