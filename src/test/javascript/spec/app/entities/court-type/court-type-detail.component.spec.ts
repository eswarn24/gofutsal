/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { GofutsalTestModule } from '../../../test.module';
import { CourtTypeDetailComponent } from '../../../../../../main/webapp/app/entities/court-type/court-type-detail.component';
import { CourtTypeService } from '../../../../../../main/webapp/app/entities/court-type/court-type.service';
import { CourtType } from '../../../../../../main/webapp/app/entities/court-type/court-type.model';

describe('Component Tests', () => {

    describe('CourtType Management Detail Component', () => {
        let comp: CourtTypeDetailComponent;
        let fixture: ComponentFixture<CourtTypeDetailComponent>;
        let service: CourtTypeService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [GofutsalTestModule],
                declarations: [CourtTypeDetailComponent],
                providers: [
                    CourtTypeService
                ]
            })
            .overrideTemplate(CourtTypeDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(CourtTypeDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CourtTypeService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new CourtType(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.courtType).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
