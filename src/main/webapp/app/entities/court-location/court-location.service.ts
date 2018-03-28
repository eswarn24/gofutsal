import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { CourtLocation } from './court-location.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<CourtLocation>;

@Injectable()
export class CourtLocationService {

    private resourceUrl =  SERVER_API_URL + 'api/court-locations';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/court-locations';

    constructor(private http: HttpClient) { }

    create(courtLocation: CourtLocation): Observable<EntityResponseType> {
        const copy = this.convert(courtLocation);
        return this.http.post<CourtLocation>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(courtLocation: CourtLocation): Observable<EntityResponseType> {
        const copy = this.convert(courtLocation);
        return this.http.put<CourtLocation>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<CourtLocation>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<CourtLocation[]>> {
        const options = createRequestOption(req);
        return this.http.get<CourtLocation[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<CourtLocation[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    search(req?: any): Observable<HttpResponse<CourtLocation[]>> {
        const options = createRequestOption(req);
        return this.http.get<CourtLocation[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<CourtLocation[]>) => this.convertArrayResponse(res));
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: CourtLocation = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<CourtLocation[]>): HttpResponse<CourtLocation[]> {
        const jsonResponse: CourtLocation[] = res.body;
        const body: CourtLocation[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to CourtLocation.
     */
    private convertItemFromServer(courtLocation: CourtLocation): CourtLocation {
        const copy: CourtLocation = Object.assign({}, courtLocation);
        return copy;
    }

    /**
     * Convert a CourtLocation to a JSON which can be sent to the server.
     */
    private convert(courtLocation: CourtLocation): CourtLocation {
        const copy: CourtLocation = Object.assign({}, courtLocation);
        return copy;
    }
}
