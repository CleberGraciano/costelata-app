import { APP_INITIALIZER, NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { SharedModule } from '@shared/shared.module';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { AuthorizationInterceptor } from '@core/guards/authorization.interceptor';

import { LOCALE_ID } from '@angular/core';
import { registerLocaleData } from '@angular/common';
import localePt from '@angular/common/locales/pt';
import { AuthenticationService } from '@core/auth/authentication.service';
import { LocalStorageService } from '@core/local-storage/local-storage.service';
import { LoaderComponent } from '@shared/components/loader/loader.component';
import { NgxPermissionsModule, NgxPermissionsService } from 'ngx-permissions';
import { AuthGuardService } from '@core/auth/auth-guard.service';
import {SidebarModule} from "primeng/sidebar";
import {PanelMenuModule} from "primeng/panelmenu";
import { CabecalhoBreadcrumbComponent } from '@shared/components/cabecalho-breadcrumb/cabecalho-breadcrumb.component';
import { ConfirmationService } from 'primeng/api';
import { DialogService } from 'primeng/dynamicdialog';

registerLocaleData(localePt, 'pt-BR');

@NgModule({
  declarations: [
    AppComponent,
    CabecalhoBreadcrumbComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    SharedModule,
    HttpClientModule,
    LoaderComponent,
    NgxPermissionsModule.forRoot(),
    SidebarModule,
    PanelMenuModule,
  ],
  providers: [
    { provide: LOCALE_ID, useValue: 'pt-BR' },
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AuthorizationInterceptor,
      multi: true,
    },
    {
      provide: APP_INITIALIZER,
      useFactory: loadPermissions,
      deps: [NgxPermissionsService, LocalStorageService],
      multi: true
    },
    AuthGuardService,
    NgxPermissionsService,
    AuthenticationService,
    LocalStorageService,
    ConfirmationService,
    DialogService
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }


export function loadPermissions(permissionService: NgxPermissionsService, localStorageService: LocalStorageService) {
  return () => {
    const usuarioLogado = localStorageService.getItem('usuario');
    const permissoes = usuarioLogado?.role;

    if (permissoes) {
      console.log(permissoes);
      const permissoesArray = Array.isArray(permissoes) ? permissoes : [permissoes];
      permissionService.loadPermissions(permissoesArray);
    }
  }
}
