/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { GofutsalTestModule } from '../../../test.module';
import { CourtLocationDeleteDialogComponent } from '../../../../../../main/webapp/app/entities/court-location/court-location-delete-dialog.component';
import { CourtLocationService } from '../../../../../../main/webapp/app/entities/court-location/court-location.service';

describe('Component Tests', () => {

    describe('CourtLocation Management Delete Component', () => {
        let comp: CourtLocationDeleteDialogComponent;
        let fixture: ComponentFixture<CourtLocationDeleteDialogComponent>;
        let service: CourtLocationService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [GofutsalTestModule],
                declarations: [CourtLocationDeleteDialogComponent],
                providers: [
                    CourtLocationService
                ]
            })
            .overrideTemplate(CourtLocationDeleteDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(CourtLocationDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CourtLocationService);
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
