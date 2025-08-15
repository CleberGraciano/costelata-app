import { Injectable } from '@angular/core';
import { ApiService } from './api.service';

const baseRef = '/omie/unidades';
const routes = {
  filter: `${baseRef}`,
};

@Injectable({
  providedIn: 'root'
})
export class UnidadesService {

  constructor(private apiService: ApiService) { }

  combo() {
    return this.apiService.get(routes.filter);
  }
}
