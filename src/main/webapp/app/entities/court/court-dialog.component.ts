import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Court } from './court.model';
import { CourtPopupService } from './court-popup.service';
import { CourtService } from './court.service';
import { CourtLocation, CourtLocationService } from '../court-location';
import { CourtType, CourtTypeService } from '../court-type';
import { User, UserService } from '../../shared';

@Component({
    selector: 'jhi-court-dialog',
    templateUrl: './court-dialog.component.html'
})
export class CourtDialogComponent implements OnInit {

    court: Court;
    isSaving: boolean;

    courtlocations: CourtLocation[];

    courttypes: CourtType[];

    users: User[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private courtService: CourtService,
        private courtLocationService: CourtLocationService,
        private courtTypeService: CourtTypeService,
        private userService: UserService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.courtLocationService
            .query({filter: 'court-is-null'})
            .subscribe((res: HttpResponse<CourtLocation[]>) => {
                if (!this.court.courtLocation || !this.court.courtLocation.id) {
                    this.courtlocations = res.body;
                } else {
                    this.courtLocationService
                        .find(this.court.courtLocation.id)
                        .subscribe((subRes: HttpResponse<CourtLocation>) => {
                            this.courtlocations = [subRes.body].concat(res.body);
                        }, (subRes: HttpErrorResponse) => this.onError(subRes.message));
                }
            }, (res: HttpErrorResponse) => this.onError(res.message));
        this.courtTypeService
            .query({filter: 'court-is-null'})
            .subscribe((res: HttpResponse<CourtType[]>) => {
                if (!this.court.courtType || !this.court.courtType.id) {
                    this.courttypes = res.body;
                } else {
                    this.courtTypeService
                        .find(this.court.courtType.id)
                        .subscribe((subRes: HttpResponse<CourtType>) => {
                            this.courttypes = [subRes.body].concat(res.body);
                        }, (subRes: HttpErrorResponse) => this.onError(subRes.message));
                }
            }, (res: HttpErrorResponse) => this.onError(res.message));
        this.userService.query()
            .subscribe((res: HttpResponse<User[]>) => { this.users = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.court.id !== undefined) {
            this.subscribeToSaveResponse(
                this.courtService.update(this.court));
        } else {
            this.subscribeToSaveResponse(
                this.courtService.create(this.court));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<Court>>) {
        result.subscribe((res: HttpResponse<Court>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: Court) {
        this.eventManager.broadcast({ name: 'courtListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackCourtLocationById(index: number, item: CourtLocation) {
        return item.id;
    }

    trackCourtTypeById(index: number, item: CourtType) {
        return item.id;
    }

    trackUserById(index: number, item: User) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-court-popup',
    template: ''
})
export class CourtPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private courtPopupService: CourtPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.courtPopupService
                    .open(CourtDialogComponent as Component, params['id']);
            } else {
                this.courtPopupService
                    .open(CourtDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
