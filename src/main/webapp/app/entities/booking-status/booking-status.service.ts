import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { BookingStatus } from './booking-status.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<BookingStatus>;

@Injectable()
export class BookingStatusService {

    private resourceUrl =  SERVER_API_URL + 'api/booking-statuses';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/booking-statuses';

    constructor(private http: HttpClient) { }

    create(bookingStatus: BookingStatus): Observable<EntityResponseType> {
        const copy = this.convert(bookingStatus);
        return this.http.post<BookingStatus>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(bookingStatus: BookingStatus): Observable<EntityResponseType> {
        const copy = this.convert(bookingStatus);
        return this.http.put<BookingStatus>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<BookingStatus>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<BookingStatus[]>> {
        const options = createRequestOption(req);
        return this.http.get<BookingStatus[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<BookingStatus[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    search(req?: any): Observable<HttpResponse<BookingStatus[]>> {
        const options = createRequestOption(req);
        return this.http.get<BookingStatus[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<BookingStatus[]>) => this.convertArrayResponse(res));
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: BookingStatus = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<BookingStatus[]>): HttpResponse<BookingStatus[]> {
        const jsonResponse: BookingStatus[] = res.body;
        const body: BookingStatus[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to BookingStatus.
     */
    private convertItemFromServer(bookingStatus: BookingStatus): BookingStatus {
        const copy: BookingStatus = Object.assign({}, bookingStatus);
        return copy;
    }

    /**
     * Convert a BookingStatus to a JSON which can be sent to the server.
     */
    private convert(bookingStatus: BookingStatus): BookingStatus {
        const copy: BookingStatus = Object.assign({}, bookingStatus);
        return copy;
    }
}
