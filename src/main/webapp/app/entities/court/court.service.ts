import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { Court } from './court.model';
import {createRequestOption} from '../../shared';

export type EntityResponseType = HttpResponse<Court>;

@Injectable()
export class CourtService {

    private resourceUrl =  SERVER_API_URL + 'api/courts';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/courts';

    constructor(private http: HttpClient) { }

    create(court: Court): Observable<EntityResponseType> {

        const copy = this.convert(court);
        return this.http.post<Court>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(court: Court): Observable<EntityResponseType> {
        const copy = this.convert(court);
        return this.http.put<Court>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<Court>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<Court[]>> {
        const options = createRequestOption(req);
        return this.http.get<Court[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Court[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    search(req?: any): Observable<HttpResponse<Court[]>> {
        const options = createRequestOption(req);
        return this.http.get<Court[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Court[]>) => this.convertArrayResponse(res));
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: Court = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<Court[]>): HttpResponse<Court[]> {
        const jsonResponse: Court[] = res.body;
        const body: Court[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to Court.
     */
    private convertItemFromServer(court: Court): Court {
        const copy: Court = Object.assign({}, court);
        return copy;
    }

    /**
     * Convert a Court to a JSON which can be sent to the server.
     */
    private convert(court: Court): Court {
        const copy: Court = Object.assign({}, court);
        return copy;
    }
}
