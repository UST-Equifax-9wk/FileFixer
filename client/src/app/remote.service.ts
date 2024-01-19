import { HttpClient, HttpHeaders, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class RemoteService {
  httpClient: HttpClient;
  baseUrl: String;
  httpOptions = {
    observe: 'response',
    headers: new HttpHeaders({ 'Content-Type': 'application/json' }),
  };

  constructor(httpClient: HttpClient) { 
    this.httpClient = httpClient;
    this.baseUrl = 'http://localhost:8080';
  }

  uploadFile(formData: FormData, fileLayout: string): Observable<HttpResponse<Object>> {
    return this.httpClient.post(this.baseUrl + `/readFile/${fileLayout}`, (formData), {
      observe: 'response', 
      withCredentials: true
    }
    )
  }

}
