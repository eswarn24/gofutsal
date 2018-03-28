import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { CourtType } from './court-type.model';
import { CourtTypeService } from './court-type.service';

@Component({
    selector: 'jhi-court-type-detail',
    templateUrl: './court-type-detail.component.html'
})
export class CourtTypeDetailComponent implements OnInit, OnDestroy {

    courtType: CourtType;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private courtTypeService: CourtTypeService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInCourtTypes();
    }

    load(id) {
        this.courtTypeService.find(id)
            .subscribe((courtTypeResponse: HttpResponse<CourtType>) => {
                this.courtType = courtTypeResponse.body;
            });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInCourtTypes() {
        this.eventSubscriber = this.eventManager.subscribe(
            'courtTypeListModification',
            (response) => this.load(this.courtType.id)
        );
    }
}
