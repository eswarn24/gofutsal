import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { CourtType } from './court-type.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<CourtType>;

@Injectable()
export class CourtTypeService {

    private resourceUrl =  SERVER_API_URL + 'api/court-types';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/court-types';

    constructor(private http: HttpClient) { }

    create(courtType: CourtType): Observable<EntityResponseType> {
        const copy = this.convert(courtType);
        return this.http.post<CourtType>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(courtType: CourtType): Observable<EntityResponseType> {
        const copy = this.convert(courtType);
        return this.http.put<CourtType>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<CourtType>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<CourtType[]>> {
        const options = createRequestOption(req);
        return this.http.get<CourtType[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<CourtType[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    search(req?: any): Observable<HttpResponse<CourtType[]>> {
        const options = createRequestOption(req);
        return this.http.get<CourtType[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<CourtType[]>) => this.convertArrayResponse(res));
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: CourtType = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<CourtType[]>): HttpResponse<CourtType[]> {
        const jsonResponse: CourtType[] = res.body;
        const body: CourtType[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to CourtType.
     */
    private convertItemFromServer(courtType: CourtType): CourtType {
        const copy: CourtType = Object.assign({}, courtType);
        return copy;
    }

    /**
     * Convert a CourtType to a JSON which can be sent to the server.
     */
    private convert(courtType: CourtType): CourtType {
        const copy: CourtType = Object.assign({}, courtType);
        return copy;
    }
}
