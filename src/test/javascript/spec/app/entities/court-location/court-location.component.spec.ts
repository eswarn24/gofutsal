/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { GofutsalTestModule } from '../../../test.module';
import { CourtLocationComponent } from '../../../../../../main/webapp/app/entities/court-location/court-location.component';
import { CourtLocationService } from '../../../../../../main/webapp/app/entities/court-location/court-location.service';
import { CourtLocation } from '../../../../../../main/webapp/app/entities/court-location/court-location.model';

describe('Component Tests', () => {

    describe('CourtLocation Management Component', () => {
        let comp: CourtLocationComponent;
        let fixture: ComponentFixture<CourtLocationComponent>;
        let service: CourtLocationService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [GofutsalTestModule],
                declarations: [CourtLocationComponent],
                providers: [
                    CourtLocationService
                ]
            })
            .overrideTemplate(CourtLocationComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(CourtLocationComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CourtLocationService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new CourtLocation(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.courtLocations[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
