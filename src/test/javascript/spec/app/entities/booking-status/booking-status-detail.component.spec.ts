/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { GofutsalTestModule } from '../../../test.module';
import { BookingStatusDetailComponent } from '../../../../../../main/webapp/app/entities/booking-status/booking-status-detail.component';
import { BookingStatusService } from '../../../../../../main/webapp/app/entities/booking-status/booking-status.service';
import { BookingStatus } from '../../../../../../main/webapp/app/entities/booking-status/booking-status.model';

describe('Component Tests', () => {

    describe('BookingStatus Management Detail Component', () => {
        let comp: BookingStatusDetailComponent;
        let fixture: ComponentFixture<BookingStatusDetailComponent>;
        let service: BookingStatusService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [GofutsalTestModule],
                declarations: [BookingStatusDetailComponent],
                providers: [
                    BookingStatusService
                ]
            })
            .overrideTemplate(BookingStatusDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(BookingStatusDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(BookingStatusService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new BookingStatus(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.bookingStatus).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
