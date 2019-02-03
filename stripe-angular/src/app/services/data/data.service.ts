import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { NotFoundError } from 'src/app/common/not-found-error';
import { BadRequestError } from 'src/app/common/bad-request-error';
import { AppError } from 'src/app/common/app-error';
const httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' })
};
@Injectable({
    providedIn: 'root'
})
export class DataService {

    constructor(private _http: HttpClient) { }
    get(url: string) {
        return this._http.get<any>(url)
            .pipe(catchError(this.handleError));
    }
    post(url: string, resource) {
        return this._http.post(url, JSON.stringify(resource), httpOptions)
            .pipe(catchError(this.handleError));
    }
    private handleError(error: Response) {
        if (error.status === 404) {
            return throwError(new NotFoundError(error));
        } else if (error.status === 400) {
            return throwError(new BadRequestError(error));
        }
        return throwError(new AppError(error));
    }
}
