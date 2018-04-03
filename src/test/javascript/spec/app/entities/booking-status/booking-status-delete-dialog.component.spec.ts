/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { GofutsalTestModule } from '../../../test.module';
import { BookingStatusDeleteDialogComponent } from '../../../../../../main/webapp/app/entities/booking-status/booking-status-delete-dialog.component';
import { BookingStatusService } from '../../../../../../main/webapp/app/entities/booking-status/booking-status.service';

describe('Component Tests', () => {

    describe('BookingStatus Management Delete Component', () => {
        let comp: BookingStatusDeleteDialogComponent;
        let fixture: ComponentFixture<BookingStatusDeleteDialogComponent>;
        let service: BookingStatusService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [GofutsalTestModule],
                declarations: [BookingStatusDeleteDialogComponent],
                providers: [
                    BookingStatusService
                ]
            })
            .overrideTemplate(BookingStatusDeleteDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(BookingStatusDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(BookingStatusService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('confirmDelete', () => {
            it('Should call delete service on confirmDelete',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        spyOn(service, 'delete').and.returnValue(Observable.of({}));

                        // WHEN
                        comp.confirmDelete(123);
                        tick();

                        // THEN
                        expect(service.delete).toHaveBeenCalledWith(123);
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });

});
