/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { GofutsalTestModule } from '../../../test.module';
import { BookingStatusComponent } from '../../../../../../main/webapp/app/entities/booking-status/booking-status.component';
import { BookingStatusService } from '../../../../../../main/webapp/app/entities/booking-status/booking-status.service';
import { BookingStatus } from '../../../../../../main/webapp/app/entities/booking-status/booking-status.model';

describe('Component Tests', () => {

    describe('BookingStatus Management Component', () => {
        let comp: BookingStatusComponent;
        let fixture: ComponentFixture<BookingStatusComponent>;
        let service: BookingStatusService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [GofutsalTestModule],
                declarations: [BookingStatusComponent],
                providers: [
                    BookingStatusService
                ]
            })
            .overrideTemplate(BookingStatusComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(BookingStatusComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(BookingStatusService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new BookingStatus(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.bookingStatuses[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
