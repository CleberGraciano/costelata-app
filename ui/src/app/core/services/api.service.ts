import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '@env/environment';

@Injectable({
  providedIn: 'root'
})
export class ApiService {

  constructor(private http: HttpClient) { }

  public post<T>(url: string, data: any, ignoreLoading?: boolean, headers?: HttpHeaders, params?: HttpParams) {
    params = this.setIgnoreLoading(ignoreLoading, params);
    return this.http.post<T>(this.prepareURL(url), data, { headers, params });
  }

  public get<T>(url: string, ignoreLoading?: boolean, params?: HttpParams, headers?: HttpHeaders) {
    params = this.setIgnoreLoading(ignoreLoading, params);
    return this.http.get<T>(this.prepareURL(url), { headers, params });
  }

  public put<T>(url: string, data?: any, ignoreLoading?: boolean, params?: HttpParams, headers?: HttpHeaders) {
    params = this.setIgnoreLoading(ignoreLoading, params);
    return this.http.put<T>(this.prepareURL(url), data, { headers, params })
  }

  public delete<T>(url: string, data?: any, ignoreLoading?: boolean, params?: HttpParams) {
    params = this.setIgnoreLoading(ignoreLoading, params);
    return this.http.request('DELETE', this.prepareURL(url), {
      params,
      body: data
    });
  }
  
  public patch<T>(url: string, data?: any, ignoreLoading?: boolean, params?: HttpParams, headers?: HttpHeaders) {
    params = this.setIgnoreLoading(ignoreLoading, params);
    return this.http.patch<T>(this.prepareURL(url), data, { headers, params })
  }

  public download(url: string, data?: any, ignoreLoading?: boolean, params?: HttpParams) {
    params = this.setIgnoreLoading(ignoreLoading, params);
    return this.http.post(this.prepareURL(url), data, { responseType: "blob", observe: 'response', params })
  }

  public downloadPost(url: string, data?: any, ignoreLoading?: boolean, params?: HttpParams) {
    params = this.setIgnoreLoading(ignoreLoading, params);
    return this.http.post(this.prepareURL(url), data, { responseType: "blob", observe: 'response', params })
  }

  public downloadGet(url: string, headers?: HttpHeaders, ignoreLoading?: boolean, params?: HttpParams) {
    params = this.setIgnoreLoading(ignoreLoading, params);
    return this.http.get(this.prepareURL(url), { responseType: "blob", observe: 'response', headers, params })
  }

  public upload<T>(url: string, formData: FormData, ignoreLoading?: boolean, params?: HttpParams) {
    params = this.setIgnoreLoading(ignoreLoading, params);
    return this.http.post<T>(this.prepareURL(url), formData);
  }

  public uploadParams<T>(url: string, formData: FormData, ignoreLoading?: boolean, params?: HttpParams) {
    params = this.setIgnoreLoading(ignoreLoading, params);
    return this.http.post<T>(this.prepareURL(url), formData, { params });
  }

  public convertObjectToParams(obj: any): HttpParams {
    let params = new HttpParams()
    if (obj === null || obj === undefined) {
      return params;
    }

    Object.keys(obj).filter(x => obj[x] != undefined && obj[x] != null && obj[x] != 'null' && obj[x] != '').forEach((x) => {
      params = params.set(x.toString(), obj[x]);
    })

    return params;
  }

  public removeNullPropertys<T>(obj: any): T {
    Object.keys(obj).filter(x => obj[x] == undefined || obj[x] == null || obj[x] == 'null' || obj[x] == '').forEach((x) => {
      delete obj[x];
    })
    return <T>obj;
  }

  private setIgnoreLoading(ignoreLoading?: boolean, params?: HttpParams): HttpParams {
    if (ignoreLoading) {
      if (!params) {
        params = new HttpParams();
      }
      params = params.append('ignoreLoading', true);
    }

    return <HttpParams>params;
  }

  private prepareURL(url: string): string {
    const api = environment.api + url;
    const apiAuth = url;
    return url.indexOf('//') != -1 ? apiAuth : api;
  }
}
