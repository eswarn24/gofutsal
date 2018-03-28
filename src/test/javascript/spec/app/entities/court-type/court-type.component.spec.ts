/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { GofutsalTestModule } from '../../../test.module';
import { CourtTypeComponent } from '../../../../../../main/webapp/app/entities/court-type/court-type.component';
import { CourtTypeService } from '../../../../../../main/webapp/app/entities/court-type/court-type.service';
import { CourtType } from '../../../../../../main/webapp/app/entities/court-type/court-type.model';

describe('Component Tests', () => {

    describe('CourtType Management Component', () => {
        let comp: CourtTypeComponent;
        let fixture: ComponentFixture<CourtTypeComponent>;
        let service: CourtTypeService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [GofutsalTestModule],
                declarations: [CourtTypeComponent],
                providers: [
                    CourtTypeService
                ]
            })
            .overrideTemplate(CourtTypeComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(CourtTypeComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CourtTypeService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new CourtType(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.courtTypes[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
