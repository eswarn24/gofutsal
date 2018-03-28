/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { GofutsalTestModule } from '../../../test.module';
import { CourtDetailComponent } from '../../../../../../main/webapp/app/entities/court/court-detail.component';
import { CourtService } from '../../../../../../main/webapp/app/entities/court/court.service';
import { Court } from '../../../../../../main/webapp/app/entities/court/court.model';

describe('Component Tests', () => {

    describe('Court Management Detail Component', () => {
        let comp: CourtDetailComponent;
        let fixture: ComponentFixture<CourtDetailComponent>;
        let service: CourtService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [GofutsalTestModule],
                declarations: [CourtDetailComponent],
                providers: [
                    CourtService
                ]
            })
            .overrideTemplate(CourtDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(CourtDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CourtService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new Court(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.court).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
