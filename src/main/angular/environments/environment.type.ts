import { LoggerLevel } from '../app/utils/logger';

export type EnvironmentType = {
  production: boolean;
  logger: LoggerLevel;
};
