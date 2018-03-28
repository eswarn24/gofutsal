/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { GofutsalTestModule } from '../../../test.module';
import { CourtComponent } from '../../../../../../main/webapp/app/entities/court/court.component';
import { CourtService } from '../../../../../../main/webapp/app/entities/court/court.service';
import { Court } from '../../../../../../main/webapp/app/entities/court/court.model';

describe('Component Tests', () => {

    describe('Court Management Component', () => {
        let comp: CourtComponent;
        let fixture: ComponentFixture<CourtComponent>;
        let service: CourtService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [GofutsalTestModule],
                declarations: [CourtComponent],
                providers: [
                    CourtService
                ]
            })
            .overrideTemplate(CourtComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(CourtComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CourtService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new Court(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.courts[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
