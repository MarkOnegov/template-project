export type UserDTO = {
  username: string;
  enabled: string;
  credentialsNonExpired: string;
  accountNonExpired: string;
  accountNonLocked: string;
  authorities: RoleDTO[];
};

export type RoleDTO = string;
