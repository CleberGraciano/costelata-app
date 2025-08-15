import { Injectable } from '@angular/core';
import { Router, ActivatedRouteSnapshot, RouterStateSnapshot, ActivatedRoute } from '@angular/router';
import { map, take } from 'rxjs/operators';
import { AuthenticationService } from '@core/auth/authentication.service';
import { Observable } from 'rxjs';
import { NgxPermissionsService } from 'ngx-permissions';

@Injectable()
export class LoginGuardService  {

    constructor(private router: Router, private authenticationService: AuthenticationService,
        private permissionsService: NgxPermissionsService, private activeRoute: ActivatedRoute) { }

    canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean> {
        return this.authenticationService.isLoggedIn
            .pipe(
                take(1),
                map((isLoggedIn: boolean) => {
                    if (isLoggedIn) {
                        if (route.queryParams['returnUrl']) {
                            let splitRoute: string[] = route.queryParams['returnUrl'].split('/');
                            this.router.navigate([`/${splitRoute[1]}`]);
                        }
                        else{
                            this.router.navigate(['/home']);
                        }

                        return false;
                    }
                    return true;
                })
            );
    }
}
