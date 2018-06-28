import {ZoneModel} from "./zone.model";

export interface ConfigModel {
  zones: ZoneModel[];
  defaultZone: string;
  currentZone: ZoneModel;
}
