import { Injectable, signal } from '@angular/core';
import { RouteAction } from '@core/enum/actions.enum';

export interface Breadcrumb {
  label: string;
  url?: string;
}

export interface HeaderButton {
  label: string;
  icon?: string;
  url: string;
}

@Injectable({ providedIn: 'root' })
export class HeaderSignalService {
  title = signal<string>('');
  actionPage = signal<RouteAction | null>(null);
  breadcrumbs = signal<Breadcrumb[]>([]);
  actionButton = signal<HeaderButton | null>(null);
}
