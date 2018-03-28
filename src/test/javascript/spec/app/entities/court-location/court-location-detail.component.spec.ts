/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { GofutsalTestModule } from '../../../test.module';
import { CourtLocationDetailComponent } from '../../../../../../main/webapp/app/entities/court-location/court-location-detail.component';
import { CourtLocationService } from '../../../../../../main/webapp/app/entities/court-location/court-location.service';
import { CourtLocation } from '../../../../../../main/webapp/app/entities/court-location/court-location.model';

describe('Component Tests', () => {

    describe('CourtLocation Management Detail Component', () => {
        let comp: CourtLocationDetailComponent;
        let fixture: ComponentFixture<CourtLocationDetailComponent>;
        let service: CourtLocationService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [GofutsalTestModule],
                declarations: [CourtLocationDetailComponent],
                providers: [
                    CourtLocationService
                ]
            })
            .overrideTemplate(CourtLocationDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(CourtLocationDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CourtLocationService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new CourtLocation(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.courtLocation).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
