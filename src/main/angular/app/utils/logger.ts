import { environment } from '../../environments/environment';

type LoggerLevelDebug = 'debug';
type LoggerLevelInfo = 'info';
type LoggerLevelWarn = 'warn';
type LoggerLevelError = 'error';
export type LoggerLevel =
  | LoggerLevelDebug
  | LoggerLevelInfo
  | LoggerLevelWarn
  | LoggerLevelError;

export class Logger {
  loggerLevel: LoggerLevel = environment.logger;
  loggerLevelNum: number;

  constructor(private clazz?: string | undefined) {
    switch (this.loggerLevel) {
      case 'debug':
        this.loggerLevelNum = 0;
        break;
      case 'info':
        this.loggerLevelNum = 1;
        break;
      case 'warn':
        this.loggerLevelNum = 2;
        break;
      default:
        this.loggerLevelNum = 3;
        break;
    }
  }

  debug(...args: any[]) {
    if (this.loggerLevelNum == 0) {
      if (this.clazz) {
        console.debug(this.clazz, ...args);
      } else {
        console.debug(...args);
      }
    }
  }

  info(...args: any[]) {
    if (this.loggerLevelNum <= 1) {
      if (this.clazz) {
        console.info(this.clazz, ...args);
      } else {
        console.info(...args);
      }
    }
  }

  warn(...args: any[]) {
    if (this.loggerLevelNum <= 2) {
      if (this.clazz) {
        console.warn(this.clazz, ...args);
      } else {
        console.warn(...args);
      }
    }
  }

  error(...args: any[]) {
    if (this.clazz) {
      console.error(this.clazz, ...args);
    } else {
      console.error(...args);
    }
  }
}
