import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { CourtType } from './court-type.model';
import { CourtTypePopupService } from './court-type-popup.service';
import { CourtTypeService } from './court-type.service';

@Component({
    selector: 'jhi-court-type-delete-dialog',
    templateUrl: './court-type-delete-dialog.component.html'
})
export class CourtTypeDeleteDialogComponent {

    courtType: CourtType;

    constructor(
        private courtTypeService: CourtTypeService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.courtTypeService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'courtTypeListModification',
                content: 'Deleted an courtType'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-court-type-delete-popup',
    template: ''
})
export class CourtTypeDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private courtTypePopupService: CourtTypePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.courtTypePopupService
                .open(CourtTypeDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
