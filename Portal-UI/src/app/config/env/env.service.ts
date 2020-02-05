import {Injectable} from '@angular/core';
import {environment} from "../../../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class EnvService {

  // The values that are defined here are the default values that can
  // be overridden by env.js

  // API gateway url
  public apiGatewayUrl = environment.apiGatewayUrl || "http://localhost:8080";

  // Whether or not to enable debug mode
  public enableDebug = true;

  constructor() {

    // Read environment variables from browser window
    const browserWindow = window || {};
    const browserWindowEnv = browserWindow['__env'] || {};

    // Assign environment variables from browser window to env
    // In the current implementation, properties from env.js overwrite defaults from the EnvService.
    // If needed, a deep merge can be performed here to merge properties instead of overwriting them.
    for (const key in browserWindowEnv) {
      if (browserWindowEnv.hasOwnProperty(key)) {
        this[key] = window['__env'][key];
      }
    }
  }

}
